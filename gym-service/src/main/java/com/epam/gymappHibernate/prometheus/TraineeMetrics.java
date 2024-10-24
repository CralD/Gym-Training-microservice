package com.epam.gymappHibernate.prometheus;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import org.springframework.stereotype.Component;

@Component
public class TraineeMetrics {
    private final Counter traineeRegistrationCounter;
    private final Counter getTraineeCounter;
    private final Counter updateTraineeCounter;
    private final Counter deleteTraineeCounter;
    private final Counter getUnassignedTrainersCounter;
    private final Counter updateTraineeTrainersCounter;
    private final Counter getTraineeTrainingsCounter;
    private final Counter activateTraineeCounter;

    public TraineeMetrics() {
        CollectorRegistry registry = new CollectorRegistry();

        traineeRegistrationCounter = Counter.build()
                .name("trainee_registration_total")
                .help("Total number of trainee registrations")
                .register(registry);

        getTraineeCounter = Counter.build()
                .name("trainee_get_total")
                .help("Total number of get trainee requests")
                .register(registry);

        updateTraineeCounter = Counter.build()
                .name("trainee_update_total")
                .help("Total number of trainee update requests")
                .register(registry);

        deleteTraineeCounter = Counter.build()
                .name("trainee_delete_total")
                .help("Total number of trainee delete requests")
                .register(registry);

        getUnassignedTrainersCounter = Counter.build()
                .name("trainee_get_unassigned_trainers_total")
                .help("Total number of get unassigned trainers requests")
                .register(registry);

        updateTraineeTrainersCounter = Counter.build()
                .name("trainee_update_trainers_total")
                .help("Total number of update trainers requests")
                .register(registry);

        getTraineeTrainingsCounter = Counter.build()
                .name("trainee_get_trainings_total")
                .help("Total number of get trainee trainings requests")
                .register(registry);

        activateTraineeCounter = Counter.build()
                .name("trainee_activate_total")
                .help("Total number of activate trainee requests")
                .register(registry);
    }

    public void incrementTraineeRegistrationCounter() {
        traineeRegistrationCounter.inc();
    }

    public void incrementGetTraineeCounter() {
        getTraineeCounter.inc();
    }

    public void incrementUpdateTraineeCounter() {
        updateTraineeCounter.inc();
    }

    public void incrementDeleteTraineeCounter() {
        deleteTraineeCounter.inc();
    }

    public void incrementGetUnassignedTrainersCounter() {
        getUnassignedTrainersCounter.inc();
    }

    public void incrementUpdateTraineeTrainersCounter() {
        updateTraineeTrainersCounter.inc();
    }

    public void incrementGetTraineeTrainingsCounter() {
        getTraineeTrainingsCounter.inc();
    }

    public void incrementActivateTraineeCounter() {
        activateTraineeCounter.inc();
    }
}
