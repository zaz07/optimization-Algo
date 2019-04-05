package aco.bigdata.fsr.ma;

public class City {
	private String name;
    private double x;
    private double y;

  
    public double distanceAlaVille(City city) {
        double x = Math.abs(this.getX() - city.getX());
        double y = Math.abs(this.getY() - city.getY());
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

	@Override
	public String toString() {
		return "City [name=" + name + ", x=" + x + ", y=" + y + "]";
	}

	public City(String name, double x, double y) {
		super();
		this.name = name;
		this.x = x;
		this.y = y;
	}

	private double getY() {
		
		return y;
	}

	private double getX() {
		
		return this.x;
	}
	public String getName() {
		
		return this.name;
	}

}