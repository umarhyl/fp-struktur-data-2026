package model;

public class LocationNode {
    private String id;
    private String name;
    private String type; // "Gudang", "Posko", "Desa"
    private int population;
    private int criticalLevel; // 1 (Low) to 5 (High)
    private double logisticsNeeded; // in tons

    public LocationNode(String id, String name, String type, int population, int criticalLevel, double logisticsNeeded) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.population = population;
        this.criticalLevel = criticalLevel;
        this.logisticsNeeded = logisticsNeeded;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getType() { return type; }
    public int getPopulation() { return population; }
    public int getCriticalLevel() { return criticalLevel; }
    public double getLogisticsNeeded() { return logisticsNeeded; }

    @Override
    public String toString() {
        return id + " - " + name + " (" + type + ")";
    }
}
