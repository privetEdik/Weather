package kettlebell.weather.service;

import kettlebell.weather.entity.Seance;
import kettlebell.weather.repository.localdb.SeanceRepositoryDb;

import java.time.LocalDateTime;
import java.util.Optional;



public class SeanceObserver implements Runnable {
	// private static final UsserService INSTANCE = new UsserService();
	//private SeanceRepositoryDb seanceRepositoryDb = SeanceRepositoryDb.getInstance();
	//private Seance seance;
//	private String keySeance;
	
//	public SeanceObserver(String keySeance) {
//		this.keySeance = keySeance;
//	}

	@Override
	public void run() {
		SeanceRepositoryDb.getInstance().clearSeance();
	}

}
