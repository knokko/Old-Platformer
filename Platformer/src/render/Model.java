package render;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import entities.Entity;
import util.PointUtils;
import util.Utils;

public class Model {
	
	public Model parent;
	
	public double rotation;
	public BufferedImage image;
	public BufferedImage image2;
	public BufferedImage imageLeft;
	public BufferedImage imageLeft2;
	public Point rotationPoint;
	public Point basePoint;
	public Entity entity;
	public String name;
	public double prevZoom;
	
	public Model(String sprite, Point base, Point rotate, Entity owner) {
		rotationPoint = rotate;
		basePoint = base;
		entity = owner;
		name = sprite.substring(sprite.lastIndexOf("/") + 1, sprite.length() - 4);
		image = Utils.getBuffImage(sprite);
		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-image.getWidth(null), 0);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		imageLeft = op.filter(image, null);
		setZoom();
	}
	
	public Model setParent(Model parentModel){
		parent = parentModel;
		return this;
	}
	
	public void paint(Graphics2D gr){
		if(prevZoom != entity.game.zoom)
			setZoom();
		prevZoom = entity.game.zoom;
		Point2D.Double base = base(gr);
		Point screen = PointUtils.gameToScreenPoint(entity.game, parent == null ? (new Point2D.Double(entity.position.x + base.x - (parent == null ? image.getWidth() / 2 : 0), entity.position.y + base.y)) : base);
		AffineTransform tx = new AffineTransform();
		tx.setToRotation(Math.toRadians(getRotation()), rotatePointX() * entity.game.zoom, rotationPoint.y * entity.game.zoom);
		AffineTransformOp op = new AffineTransformOp(tx, 2);
		gr.drawImage(entity.facingLeft ? imageLeft2 : image2, op, screen.x, screen.y);
	}
	
	public void paintSimple(Graphics2D gr){
		if(prevZoom != entity.game.zoom)
			setZoom();
		prevZoom = entity.game.zoom;
		Point2D.Double base = base(gr);
		Point screen = PointUtils.gameToScreenPoint(entity.game, parent == null ? (new Point2D.Double(entity.position.x + base.x - (parent == null ? image.getWidth() / 2 : 0), entity.position.y + base.y)) : base);
		gr.drawImage(entity.facingLeft ? imageLeft2 : image2, screen.x, screen.y, null);
	}
	
	public Point2D.Double base(Graphics2D gr){
		if(parent != null){
			double dx = basePoint.x + rotationPoint.x - parent.rotationPoint.x;
			if(entity.facingLeft)
				dx = -dx;
			double dy = basePoint.y + rotationPoint.y - parent.rotationPoint.y;
			double distance = Math.hypot(dx, dy);
			double extraR = Math.toDegrees(Math.atan(dy / dx));
			double r = parent.getRotation() + extraR;
			if(entity.facingLeft)
				r -= 180;
			double ex = Math.cos(Math.toRadians(r)) * distance;
			double ey = -Math.sin(Math.toRadians(r)) * distance;
			Point2D.Double parentBase = parent.base(gr);
			Point2D.Double test = new Point2D.Double(entity.position.x + parentBase.x + parent.rotatePointX() - parent.image.getWidth() / 2, entity.position.y + parentBase.y - parent.rotationPoint.y);
			return new Point2D.Double(test.x + ex - rotatePointX(), test.y + ey + rotationPoint.y);
		}
		return new Point2D.Double(entity.facingLeft ? -basePoint.x : basePoint.x, basePoint.y);
	}
	
	public double rotatePointX(){
		return entity.facingLeft ? image.getWidth() - rotationPoint.x : rotationPoint.x;
	}
	
	public void setImage(String sprite){
		name = sprite.substring(sprite.lastIndexOf("/") + 1, sprite.length() - 4);
		image = Utils.getBuffImage(sprite);
		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-image.getWidth(null), 0);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		imageLeft = op.filter(image, null);
		setZoom();
	}
	
	public void setZoom(){
		image2 = scaleToSize(image);
		imageLeft2 = scaleToSize(imageLeft);
	}
	
	public double getRotation(){
		if(parent != null)
			return rotation + parent.getRotation();
		return rotation;
	}
	
	private BufferedImage scaleToSize(BufferedImage uploadImage){
		  AffineTransform atx=new AffineTransform();
		  atx.scale(entity.game.zoom, entity.game.zoom);
		  AffineTransformOp afop=new AffineTransformOp(atx,AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		  uploadImage=afop.filter(uploadImage,null);
		  return uploadImage;
	}
}
