
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import java.io.File;


class Vector{
	public static final Vector ZERO = new Vector(0,0,0);
	public static final Vector i = new Vector(1,0,0);
	public static final Vector j = new Vector(0,1,0);
	public static final Vector k = new Vector(0,0,1);
	
	public double x;
	public double y;
	public double z;
	
	Vector(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public static Vector sum(Vector v1, Vector v2){
		return new Vector(v1.x+v2.x, v1.y+v2.y, v1.z+v2.z);
	}
	public static Vector negate(Vector v1, Vector v2){
		return new Vector(v1.x-v2.x, v1.y-v2.y, v1.z-v2.z);
	}
	public static Vector multiply(Vector v, double k){
		return new Vector(v.x*k, v.y*k, v.z*k);
	}
	public static double scalarMul(Vector v1, Vector v2){
		return v1.x*v2.x + v1.y*v2.y + v1.z*v2.z;
	}
	public static Vector vectorMul(Vector v1, Vector v2){
		return new Vector(v1.y*v2.z - v1.z*v2.y, v1.z*v2.x - v1.x*v2.z, v1.x*v2.y - v1.y-v2.x);
	}
	public static double getLength(Vector v){
		return Math.sqrt(v.x*v.x + v.y*v.y + v.z*v.z);
	}
	public static Vector getOne(Vector v){
		return Vector.multiply(v, 1/Vector.getLength(v));
	}
	public static Vector rotateVector(Vector v, double a_z, double a_y){
		Vector a = new Vector(v.x, v.y, v.z);
		
		/*double l = Vector.getLength(a);
		double k = Math.cos(a_y) - v.z* Math.sin(a_y)/l;
		a = Vector.multiply(a, k);
		a.z = v.z*Math.cos(a_y) + l* Math.sin(a_y);
		
		double x = a.x;
		double y = a.y;
		a.x = x*Math.cos(a_z) + y * Math.sin(a_z);
		a.y = y*Math.cos(a_z) - x * Math.sin(a_z);*/
		
		double cos = a.x;
		double sin = a.z;
		a.x = cos*Math.cos(a_y) - sin*Math.sin(a_y);
		a.z = cos*Math.sin(a_y) + sin*Math.cos(a_y);
		
		cos = a.x;
		sin = a.y;
		a.x = cos*Math.cos(a_z) - sin*Math.sin(a_z);
		a.y = cos*Math.sin(a_z) + sin*Math.cos(a_z);
		
		return a;
	}
	public String toString(){return this.x+" "+this.y+" "+this.z;}
	
	public Vector reflectVector(Vector a, Vector n){
		Vector r = new Vector(a.x, a.y, a.z);
		if(Math.abs(n.x) == 1) r.x = -a.x;
		if(Math.abs(n.y) == 1) r.y = -a.y;
		if(Math.abs(n.z) == 1) r.z = -a.z;
		return r;
	}
}

abstract class Object{
	public Vector cord;
	
	abstract public double getMinIntersection(Vector c, Vector v);
	abstract public double getMaxIntersection(Vector c, Vector v);
	abstract public Vector getNormal(Vector dote);
}


class Camera{
	public Vector cord;
	public Vector alpha;
	
	public double a_z = 0;
	public double a_y = 0;
	
	Camera(Vector c, Vector n){
		this.cord = c;
		this.alpha = n;
	}
}

class Skybox{
	public BufferedImage skybox = null;
	
	Skybox(String path_skybox){
		try{
			skybox = ImageIO.read(new File(path_skybox));
		}catch(Exception e){System.err.println(e);}
		
	}
	
	public int getPixel(Vector v){
		if(skybox == null) return 0;
		double d_y = v.z;
		double plosk = Math.sqrt(v.x*v.x + v.y*v.y);
		double d_x_sin = v.x/plosk;
		double d_x_cos = v.y/plosk;
		
		double phi = Math.asin(d_x_sin);
		if(d_x_cos < 0){
			phi = Math.PI-Math.asin(d_x_sin);
		}
		if(phi < 0) phi += Math.PI*2;
		
		int y = (int)((0.5-Math.asin(d_y)/Math.PI)*skybox.getHeight());
		if(y<0) y = 0;
		if(y>skybox.getHeight()) y= skybox.getHeight();
		int x = (int)(phi/(2*Math.PI)*skybox.getWidth());
		if(x<0) x = 0;
		if(x>skybox.getWidth()) x = skybox.getWidth();
		int a;
		try{
			a = skybox.getRGB(x,y);
		}catch(Exception e){a = 0;}
		return a;
	}
}