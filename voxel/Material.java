
class Material{
	public static Material AIR = new Material(0,0,0, 0);
	public static Material OBJ = new Material(255,255,255, 255);
	
	
	public int red;
	public int blue;
	public int green;
	public int alpha;
	
	Material(int r, int g, int b, int a){this.red = r; this.green = g; this.blue = b;this.alpha = a;}
	
	public int getColor(){
		return (this.red*256 + this.green)*256 + this.blue;
	}
	public static int getUnityColor(double k){
		int d = (int)(255*k);
		return (d*256 + d)*256 + d;
	}
}
/*class Light_Material extends Material{
	
}*/