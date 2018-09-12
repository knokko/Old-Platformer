package main;

import java.awt.event.*;
import java.awt.geom.Point2D;

import actions.Gadget;
import actions.Spell;
import colliders.ColliderBox;
import util.PointUtils;
import entities.Entity;
import entities.Player;
import entities.TileEntity;
import entities.projectiles.Axe;
import entities.projectiles.GrablingHook;
import entities.projectiles.Projectile;
import entities.projectiles.Spike;

public class Controller implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {
	
	public final Game game;
	public final Player player;
	
	/**
	 * -0 : left-
	 * -1 : up-
	 * -2 : right-
	 * -3 : S-
	 * -4 : F-
	 * -5 : X-
	 * -6 : H-
	 * -7 : R-
	 * -8 : shift-
	 * -9 : B-
	 * -10: K-
	 */
	public final boolean[] pressedKeys = new boolean[11];
	public final boolean[] pressedMouse = new boolean[3];
	
	public int scrolling;
	
	public Controller(Game game, Player player) {
		this.game = game;
		this.player = player;
	}

	public void mouseWheelMoved(MouseWheelEvent event) {
		scrolling = event.getWheelRotation();
	}

	public void mouseDragged(MouseEvent e) {}

	public void mouseMoved(MouseEvent e) {}

	public void mouseClicked(MouseEvent event) {
		if(game.gui != null){
			if(event.getButton() == 1)
				game.gui.click(event.getPoint());
			if(event.getButton() == 3)
				game.gui.rightClick(event.getPoint());
		}
	}

	public void mousePressed(MouseEvent event) {
		if(event.getButton() == 1)
			pressedMouse[0] = true;
		if(event.getButton() == 3)
			pressedMouse[1] = true;
		if(event.getButton() == 2)
			pressedMouse[2] = true;
	}

	public void mouseReleased(MouseEvent event) {
		if(event.getButton() == 1)
			pressedMouse[0] = false;
		if(event.getButton() == 3)
			pressedMouse[1] = false;
		if(event.getButton() == 2)
			pressedMouse[2] = false;
	}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void keyTyped(KeyEvent event) {
		if(game.gui != null)
			game.gui.keyTyped(event);
	}

	public void keyPressed(KeyEvent event) {
		int c = event.getKeyCode();
		if(c == 37 || c == 65)
			pressedKeys[0] = true;
		if(c == 38 || c == 87 || c == 32)
			pressedKeys[1] = true;
		if(c == 39 || c == 68)
			pressedKeys[2] = true;
		if(c == 83)
			pressedKeys[3] = true;
		if(c == 70)
			pressedKeys[4] = true;
		if(c == 88)
			pressedKeys[5] = true;
		if(c == 72)
			pressedKeys[6] = true;
		if(c == 82)
			pressedKeys[7] = true;
		if(c == 16)
			pressedKeys[8] = true;
		if(c == 66)
			pressedKeys[9] = true;
		if(c == 75)
			pressedKeys[10] = true;
		if(c == 27)
			game.backToMenu();
	}

	public void keyReleased(KeyEvent event) {
		int c = event.getKeyCode();
		if(c == 37 || c == 65)
			pressedKeys[0] = false;
		if(c == 38 || c == 87 || c == 32)
			pressedKeys[1] = false;
		if(c == 39 || c == 68)
			pressedKeys[2] = false;
		if(c == 83)
			pressedKeys[3] = false;
		if(c == 70)
			pressedKeys[4] = false;
		if(c == 88)
			pressedKeys[5] = false;
		if(c == 72)
			pressedKeys[6] = false;
		if(c == 82)
			pressedKeys[7] = false;
		if(c == 16)
			pressedKeys[8] = false;
		if(c == 66)
			pressedKeys[9] = false;
		if(c == 75)
			pressedKeys[10] = false;
	}
	
	public void update(){
		if(pressedKeys[3] && player.world.isAllowed(Gadget.SPIKE_GUN))
			player.setWeapon("spike gun");
		if(pressedKeys[4] && player.world.isAllowed(Spell.FLASH))
			player.setSpell("flash");
		if(pressedKeys[5] && player.world.isAllowed(Gadget.AXE))
			player.setWeapon("axe");
		if(pressedKeys[6] && player.world.isAllowed(Gadget.HOOK_GUN))
			player.setWeapon("hook gun");
		if(pressedKeys[7] && player.world.isAllowed(Spell.ROCK))
			player.setSpell("rock");
		if(pressedKeys[9] && player.world.isAllowed(Spell.BOOST))
			player.setSpell("boost");
		if(pressedKeys[10] && player.world.isAllowed(Gadget.BLADE))
			player.setWeapon("blade");
		if(game.pause)
			return;
		if(pressedKeys[0])
			player.moveX = -1;
		if(pressedKeys[1] && player.onGround)
			player.jump(45000);
		if(pressedKeys[2])
			 player.moveX = 1;
		if(player.cooldown <= 0){
			if(weaponClick("spike gun")){
				Spike spike = new Spike(player, PointUtils.screenToGamePoint(game, game.getMousePosition()));
				player.world.spawnEntity(spike);
				player.cooldown = 10;
				rotatePlayerArm(spike);
				player.moveX = 0;
				player.setWeapon("spike gun");
			}
			else if(weaponClick("axe")){
				if(player.axes > 0){
					Axe axe = new Axe(player, PointUtils.screenToGamePoint(game, game.getMousePosition()));
					player.world.spawnEntity(axe);
					player.moveX = 0;
					player.cooldown = 20;
					--player.axes;
					rotatePlayerArm(axe);
					if(player.axes > 0)
						player.setWeapon("axe");
					else
						player.setWeapon("arm");
				}
			}
			else if(weaponClick("hook gun")){
				clearHook();
				GrablingHook hook = new GrablingHook(player, PointUtils.screenToGamePoint(game, game.getMousePosition()));
				player.world.spawnEntity(hook);
				player.setWeapon("hook gun");
				player.cooldown = 20;
				player.moveX = 0;
				rotatePlayerArm(hook);
			}
			else if(weaponClick("blade")){
				double range = 60;
				Point2D.Double mouse = PointUtils.screenToGamePoint(game, game.getMousePosition());
				player.cooldown = 20;
				player.moveX = 0;
				double angle = Math.atan2(mouse.y - player.position.y, mouse.x - player.position.x);
				Point2D.Double target = new Point2D.Double(player.position.x + Math.cos(angle) * range, player.position.y + Math.sin(angle) * range);
				ColliderBox collider = new ColliderBox(player.position.x > target.x ? target.x : player.position.x, player.position.x < target.x ? target.x : player.position.x, player.position.y > target.y ? target.y : player.position.y, player.position.y < target.y ? target.y : player.position.y);
				int t = 0;
				while(t < player.world.entities.size()){
					Entity entity = player.world.entities.get(t);
					if(entity != player && collider.hit(entity.getCollider())){
						entity.attack(player.position, 400);
					}
					++t;
				}
				player.arm2().rotation = -Math.toDegrees(angle) - player.body.rotation + 40;
				player.goalAngle = -Math.toDegrees(angle) - player.body.rotation - 40;
				double mx = Math.cos(angle) * -Math.sin(Math.toRadians(game.world.getGravityDirection(player.position))) + Math.sin(angle) * Math.cos(Math.toRadians(game.world.getGravityDirection(player.position)));
				if(mx < 0){
					player.facingLeft = true;
					player.arm2().rotation -= 180;
					player.goalAngle -= 180;
				}
				if(mx > 0)
					player.facingLeft = false;
			}
		}
		if((spellClick("flash")) && player.mana >= 100 && !player.cooldown2){
			Point2D.Double target = PointUtils.screenToGamePoint(game, game.getMousePosition());
			if(player.hook != null)
				player.hook.maxLength += 150;
			if(target.distance(player.position) <= 150){
				if(player.teleport(target)){
					player.mana -= 100;
					player.cooldown2 = true;
				}
				else if(player.hook != null)
					player.hook.maxLength -= 150;
			}
			else {
				double distanceX = target.x - player.position.x;
				double distanceY = target.y - player.position.y;
				double angle = Math.atan2(distanceY, distanceX);
				int dx = (int) (Math.cos(angle) * 150);
				int dy = (int) (Math.sin(angle) * 150);
				if(player.teleport(new Point2D.Double(player.position.x + dx, player.position.y + dy))){
					player.mana -= 100;
					player.cooldown2 = true;
					if(player.hook != null)
						player.hook.maxLength -= 150;
				}
			}
			player.setSpell("flash");
		}
		if((spellClick("rock")) && player.mana >= 100 && !player.cooldown2){
			TileEntity entity = new TileEntity(game, player.world, PointUtils.screenToGamePoint(game, game.getMousePosition()), "sprites/tiles/rock.png", 30, 90);
			entity.move(0, -entity.position.y - 1000);
			game.world.spawnEntity(entity);
			game.player.mana -= 100;
			player.cooldown2 = true;
			player.setSpell("rock");
		}
		if(spellClick("boost") && player.mana >= 100 && !player.cooldown2){
			Point2D.Double target = PointUtils.screenToGamePoint(game, game.getMousePosition());
			double rotation = Math.toDegrees(Math.atan2(target.y - player.position.y, target.x - player.position.x));
			player.addAimedForce(rotation, 40000);
			player.mana -= 100;
			player.cooldown2 = true;
		}
		if(scrolling != 0){
			if(pressedKeys[8] || player.hook == null){
				if(scrolling > 0 && game.getWidth() / (game.zoom / 1.2) <= game.world.width() * 60 && game.getHeight() / (game.zoom / 1.2) <= game.world.height() * 60)
					game.zoom /= 1.2;
				else if(scrolling < 0)
					game.zoom *= 1.2;
			}
			else if(player.hook != null){
				if(scrolling > 0){
					player.hook.maxLength -= scrolling * 25;
					if(player.getCollider().distanceTo(player.hook.getCollider()) > player.hook.maxLength)
						player.hook.pull(scrolling * 25);
				}
				else
					player.hook.maxLength -= scrolling * 25;
			}
			scrolling = 0;
		}
		if(pressedMouse[2])
			clearHook();
	}
	
	public void updateZoom(){
		if(scrolling != 0){
			if(scrolling > 0)
				game.zoom /= 1.2;
			else
				game.zoom *= 1.2;
		}
		scrolling = 0;
	}
	
	private boolean weaponClick(String weapon){
		return player.hasWeapon(weapon) && pressedMouse[0];
	}
	
	private boolean spellClick(String spell){
		return player.hasSpell(spell) && pressedMouse[1];
	}
	
	private void clearHook(){
		if(player.hook != null){
			player.hook.inactive = true;
			player.hook = null;
		}
	}
	
	private void rotatePlayerArm(Projectile projectile){
		player.arm2().rotation = projectile.model().rotation - player.body.rotation;
		double mx = projectile.motionX * -Math.sin(Math.toRadians(game.world.getGravityDirection(player.position))) + projectile.motionY * Math.cos(Math.toRadians(game.world.getGravityDirection(player.position)));
		if(mx < 0){
			player.facingLeft = true;
			player.arm2().rotation -= 180;
		}
		if(mx > 0)
			player.facingLeft = false;
	}
}
