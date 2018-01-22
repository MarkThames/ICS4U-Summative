package LifeSimulator;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
interface MenuUpdateListener {
    void onMenuUpdate();
}
abstract class MenuUpdateEvent implements ActionListener {
    protected List<MenuUpdateListener>listeners=new ArrayList<>();
    public MenuUpdateEvent(){
    }
    public void addListener(MenuUpdateListener mul){
        listeners.add(mul);
    }
}