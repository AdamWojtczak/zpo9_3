import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

import static java.lang.Thread.sleep;

public class Main {
    private static int licznik_wytrwalych_widzow = 0;
    public static String[] wsciekly_tlum = { "Frajerzy", "Pieniedzy za bilety nie oddaja", "Oddajcie pieniadze"};
    static List<Widz> listaWidzow = new ArrayList<>();
    static ExecutorService widzowie_scheduler = Executors.newFixedThreadPool(30);
    public static void main(String[] args) throws Exception {

        final ExecutorService scheduler = Executors.newFixedThreadPool(100);
        List<Osoba> osobaList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            osobaList.add(new Osoba());
       }
        List<Future<Integer>> lista_watkow_osob = scheduler.invokeAll(osobaList);
        for(Future<Integer> f : lista_watkow_osob)
        {
            f.get();
        }
        if (listaWidzow.size() >= 5)
        {
            System.out.println("Start filmu");
            List<Future<Integer>> returny = widzowie_scheduler.invokeAll(listaWidzow);
            //System.out.println(returny);
            widzowie_scheduler.shutdown();
            System.out.println("Koniec filmu");
        }
        else
        {
            System.out.println("Film sie nie odbedzie");
        }
        scheduler.shutdown();
    }

    private static class Widz implements Callable<Integer>{
        @Override
        public Integer call() throws Exception {

            try { sleep(2000); }
            catch (InterruptedException e) { e.printStackTrace(); }
            int x = new Random().nextInt(10);
            if (x >= 3 ) {
                licznik_wytrwalych_widzow++;
            }

            try {
                sleep(2000); //polowa filmu 2s
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (licznik_wytrwalych_widzow < 5)
            {
                x = new Random().nextInt(3);
                System.out.println(wsciekly_tlum[x]);
            }

            return 0;
        }
    }
    private static class Osoba implements Callable<Integer>{
        @Override
        public Integer call() throws Exception {
            int x = (new Random().nextInt(4)) + 1;
            System.out.println("Osoba watek");
            try {
                sleep(x * 1000); //czekanie losowej ilosci miedzy 1 a 4s
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            x = new Random().nextInt(100);
            if (x >= 0 && x < 6) {
                System.out.println("WYBRALEM POZYTYWNIE");
                listaWidzow.add(new Widz());
                return 1;
            }
            return 0;
        }
    }
}
