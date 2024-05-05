package kettlebell.weather.service;

import kettlebell.weather.repository.localdb.SeanceRepositoryDb;

public class SeanceObserver implements Runnable {

    @Override
    public void run() {

        SeanceRepositoryDb.getInstance().clearSeance();
    }

}
