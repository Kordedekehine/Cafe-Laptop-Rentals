package laptopRentage.rentages.model;

public enum Color {

    white,black,ash;

    @Override
    public String toString() {
        switch (this){
            case white:return "WHITE";
            case black:return "BLACK";
            case ash:return "ASH";
            default:return null;
        }
    }
}
