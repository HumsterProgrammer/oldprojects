

abstract class Light{
}

class PointLight extends Light{
	public Vector cord;
	
	PointLight(Vector c){
		this.cord = c;
	}
}