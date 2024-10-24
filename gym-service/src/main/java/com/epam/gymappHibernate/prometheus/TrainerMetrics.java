package com.epam.gymappHibernate.prometheus;



import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import jdk.jfr.Category;
import org.springframework.stereotype.Component;

@Component
public class TrainerMetrics {
    private final Counter trainerRegistrationCounter;
    private final Gauge trainerUpdateGauge;
    private final Counter getTrainerCounter;
    private final Counter updateTrainerCounter;
    private final Counter getTrainerTrainingsCounter;
    private final Counter activateTrainerCounter;

    public TrainerMetrics() {
        CollectorRegistry registry = new CollectorRegistry();

        trainerRegistrationCounter = Counter.build()
                .name("trainer_registration_total")
                .help("Total number of trainer registrations")
                .register(registry);

        trainerUpdateGauge = Gauge.build()
                .name("trainer_update_status")
                .help("Trainer update status (0 = failed, 1 = success)")
                .register(registry);
        getTrainerCounter = Counter.build()
                .name("trainer_get_total")
                .help("Total number of get trainer requests")
                .register(registry);

        updateTrainerCounter = Counter.build()
                .name("trainer_update_total")
                .help("Total number of trainer update requests")
                .register(registry);

        getTrainerTrainingsCounter = Counter.build()
                .name("trainer_get_trainings_total")
                .help("Total number of get trainer trainings requests")
                .register(registry);

        activateTrainerCounter = Counter.build()
                .name("trainer_activate_total")
                .help("Total number of activate trainer requests")
                .register(registry);
    }

    public void incrementTrainerRegistrationCounter() {
        trainerRegistrationCounter.inc();
    }

    public void setTrainerUpdateStatus(boolean success) {
        if (success) {
            trainerUpdateGauge.set(1);
        } else {
            trainerUpdateGauge.set(0);
        }
    }
    public void incrementGetTrainerCounter() {
        getTrainerCounter.inc();
    }

    public void incrementUpdateTrainerCounter() {
        updateTrainerCounter.inc();
    }

    public void incrementGetTrainerTrainingsCounter() {
        getTrainerTrainingsCounter.inc();
    }

    public void incrementActivateTrainerCounter() {
        activateTrainerCounter.inc();
    }
}
