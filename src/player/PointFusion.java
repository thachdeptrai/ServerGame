package player;

/**
 * @build by NgocThach
 */
public class PointFusion {

    private double HpFusion;
    private double MpFusion;
    private double DameFusion;
    private Player player;

    public PointFusion(Player player) {
        this.player = player;
        this.HpFusion = 0; // Giá trị mặc định
        this.MpFusion = 0;  // Giá trị mặc định
        this.DameFusion = 0; // Giá trị mặc định
    }

    public double getHpFusion() {
        return this.HpFusion;
    }

    public void setHpFusion(double HpFusion) {
        this.HpFusion = HpFusion;
    }

    public double getMpFusion() {
        return this.MpFusion;
    }

    public void setMpFusion(double MpFusion) {
        this.MpFusion = MpFusion;
    }

    public double getDameFusion() {
        return this.DameFusion;
    }

    public void setDameFusion(double DameFusion) {
        this.DameFusion = DameFusion;
    }

    public void update() {

    }

    public void dispose() {
        this.player = null;
    }
}
