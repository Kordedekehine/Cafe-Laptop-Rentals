package laptopRentage.rentages.model;

public enum Cores {

    corei5,corei6,corei7,macAir,macBookPro;


    @Override
    public String toString() {
        switch(this){
            case corei5: return "CoreI5";
            case corei6: return "CoreI6";
            case corei7: return "CoreI7";
            case macAir:return "MacBook Air";
            case macBookPro:return "MacBook Pro";
            default:return null;
        }
    }
}
