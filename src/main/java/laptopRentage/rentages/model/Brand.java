package laptopRentage.rentages.model;

public enum Brand {
    MacBook,HP,Lenovo,Asus,Dell,Toshiba;

    /**
     * ALL OUR LAPTOPS ARE 8TH GEN LAPTOP,SO YOU KNOW WHAT TO EXPECT
     * @return
     */
    @Override
    public String toString() {
       switch (this){
           case MacBook:return "MacBook";
           case HP:return "HP";
           case Lenovo:return "Lenovo";
           case Asus:return "Asus";
           case Dell:return "Dell";
           case Toshiba:return "Toshiba";
           default:return null;
       }
    }
}
