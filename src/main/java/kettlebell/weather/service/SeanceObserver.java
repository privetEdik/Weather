package kettlebell.weather.service;

import kettlebell.weather.repository.SeanceRepository;

public class SeanceObserver implements Runnable {

    @Override
    public void run() {

        SeanceRepository.getInstance().clearSeance();
    }

}
