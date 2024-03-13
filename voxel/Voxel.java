


class Voxel{
	private Voxel[] daughter;
	private int depth;
	private Material mat;
	
	public Vector cord;
	public int size;
	
	
	
	Voxel(Vector c, int s, Material m, int d, int max_depth){
		this.cord = c;
		this.size = s;
		this.mat = m;
		this.depth = d;
		
		this.daughter = new Voxel[8];
		if(d == max_depth){
			for(int i = 0; i< 8; i++){
				this.daughter[i] = null;
			}
		}else{
			for(int i = 0; i < 8; i++){
				Vector c_1 = new Vector(i/2%2,i%2,i/4);
				c_1 = Vector.multiply(c_1, s/2);
				this.daughter[i] = new Voxel(c_1 ,s/2 ,m,d+1, max_depth);// must be setted in future
			}
		}
	}
	
	public void setMaterial(Material x){this.mat = x;}
	public Material getMaterial(){return this.mat;}
	
	public Voxel getVoxel(Vector c){
		//System.out.println(c.toString());
		for(int i = 0; i< 8; i++){
			if(daughter[i] == null){continue;}
			if(c.x >= daughter[i].cord.x && c.x<= daughter[i].cord.x+daughter[i].size){
				if(c.y >= daughter[i].cord.y && c.y<= daughter[i].cord.y+daughter[i].size){
					if(c.z >= daughter[i].cord.z && c.z<= daughter[i].cord.z+daughter[i].size){
						//System.out.println(i);
						return daughter[i].getVoxel(c);
					}
				}
			}
		}
		return this;
	}
	public void setVoxelMaterial(Vector c, Material m){
		getVoxel(c).setMaterial(m);
	}
	
	
	public RenderReturn renderVoxel(Vector camera, Vector alpha, int max_depth, Material start_material){
		double t = getIntersection(camera, alpha, this.cord, this.size);
		if(max_depth == this.depth || t <0){
			if(this.mat == start_material) t = -1;
			return new RenderReturn(t, this.cord, this.size);
		}
		
		RenderReturn min_t = new RenderReturn(-1, Vector.ZERO, 1);
		for(int i = 0; i< 8; i++){
			if(daughter[i] == null) continue;
			t = getIntersection(camera, alpha, daughter[i].cord, daughter[i].size); // Если пересекается
			if(t>=0){
				RenderReturn x = daughter[i].renderVoxel(camera, alpha, max_depth, start_material);// проверяет дочерние воксели
				if(x.t<min_t.t && x.t>=0 || min_t.t == -1) min_t = x;
			}
		}
		return min_t;
	}
	
	//function of get Intersection with this voxel object. Can be optimoze in future
	public static double getIntersection(Vector camera, Vector alpha, Vector cord, int size){
		Vector d = Vector.negate(cord, camera);
		
		double tx1 = d.x/alpha.x;
		double tx2 = (d.x + size)/alpha.x;
		if(tx1>tx2){
			double t = tx1;
			tx1 = tx2;
			tx2 = t;
		}
		double tx = tx1;
		if(tx<0) tx = tx2;
		
		double ty1 = (d.y + size)/alpha.y;
		double ty2 = d.y/alpha.y;
		if(ty1>ty2){
			double t = ty1;
			ty1 = ty2;
			ty2 = t;
		}
		double ty = ty1;
		if(ty<0) ty = ty2;
		
		double tz1 = (d.z + size)/alpha.z;
		double tz2 = d.z/alpha.z;
		if(tz1>tz2){
			double t = tz1;
			tz1 = tz2;
			tz2 = t;
		}
		double tz = tz1;
		if(tx<0) tz = tz2;
		
		double t = -1;
		if(tx>= ty1 && tx<= ty2 && tx>= tz1 && tx<= tz2 && (tx<t || t == -1)) t = tx;
		if(ty>= tx1 && ty<= tx2 && ty>= tz1 && ty<= tz2 && (ty<t || t == -1)) t = ty;
		if(tz>= ty1 && tz<= ty2 && tz>= tx1 && tz<= tx2 && (tz<t || t == -1)) t = tz;
		return t;
	}
}

class RenderReturn{
	public double t = 0;
	public Vector cord = Vector.ZERO;
	public int size = 0;
	
	RenderReturn(double x, Vector y, int z){
		this.t = x;
		this.cord = y;
		this.size = z;
	}
	public Vector getNormal(Vector dote){
		if(Math.abs(dote.x - this.cord.x) < 0.01) return new Vector(-1, 0,0);
		if(Math.abs(dote.x - this.cord.x-size) < 0.01) return Vector.i;
		if(Math.abs(dote.y - this.cord.y)<0.01) return new Vector(0, -1,0);
		if(Math.abs(dote.y - this.cord.y-size)<0.01) return Vector.j;
		if(Math.abs(dote.z - this.cord.z)<0.01) return new Vector(0,0,-1);
		if(Math.abs(dote.z - this.cord.z-size)<0.01) return Vector.k;
		return Vector.i;
	}
}