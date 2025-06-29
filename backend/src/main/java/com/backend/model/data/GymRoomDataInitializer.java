package com.backend.model.data;

import com.backend.model.GymRoom;
import com.backend.repository.GymRoomRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class GymRoomDataInitializer implements CommandLineRunner {

    private final GymRoomRepository roomRepository;

    public GymRoomDataInitializer(GymRoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (roomRepository.count() == 0) {
            roomRepository.save(new GymRoom(
                    "Stay Fit București - Mall Vitan",
                    "Sala modernă din Mall Vitan, cu zonă de cycling, clase de HIIT și aparat de analiză corporală.",
                    "Calea Vitan, nr. 55-59",
                    "București",
                    "Vitan",
                    44.418257, 26.140147,
                    "06:00-23:00",
                    "+40 021 123 4567",
                    "contact@stayfit-vitan.ro",
                    "stayfit_carolina.jpg",
                    120
            ));

            roomRepository.save(new GymRoom(
                    "X Body Gym Herăstrău",
                    "Antrenamente EMS și funcționale, perfect pentru stilul de viață activ din nordul capitalei.",
                    "Șoseaua Nordului, nr. 20",
                    "București",
                    "Herăstrău",
                    44.475030, 26.082940,
                    "07:00-22:00",
                    "+40 021 789 1011",
                    "office@xbody-herastrau.ro",
                    "xbodygym.jpg",
                    60
            ));

            roomRepository.save(new GymRoom(
                    "Flex Zone Fitness Băneasa",
                    "Spațiu elegant de fitness cu lounge, dușuri moderne și clase de pilates și yoga.",
                    "Bulevardul Aerogării, nr. 12A",
                    "București",
                    "Băneasa",
                    44.501234, 26.083701,
                    "08:00-21:00",
                    "+40 021 555 1234",
                    "info@flexzone-baneasa.ro",
                    "flexzone.jpg",
                    80
            ));

            roomRepository.save(new GymRoom(
                    "Powerhouse Gym Militari",
                    "Sală specializată în culturism, cu antrenori personali și zonă de forță extrem.",
                    "Strada Apusului, nr. 17",
                    "București",
                    "Militari",
                    44.435120, 25.980300,
                    "05:00-24:00",
                    "+40 021 321 6543",
                    "office@powerhousemilitari.ro",
                    "powerhouse.jpg",
                    150
            ));

            roomRepository.save(new GymRoom(
                    "UrbanFit by Sportland Titan",
                    "Sala UrbanFit Titan oferă o zonă generoasă de stretching și aparate cardio de ultimă generație.",
                    "Bulevardul Nicolae Grigorescu, nr. 33",
                    "București",
                    "Titan",
                    44.422560, 26.171820,
                    "06:30-22:30",
                    "+40 021 456 7890",
                    "contact@urbanfit-titan.ro",
                    "urbanfit.jpg",
                    100
            ));

            // SALI NOI ADAUGATE

            roomRepository.save(new GymRoom(
                    "Downtown Fit Unirii",
                    "O sală de top cu design industrial, aparate noi și clase live transmise online.",
                    "Strada Halelor, nr. 7",
                    "București",
                    "Unirii",
                    44.428500, 26.102300,
                    "07:00-23:00",
                    "+40 021 876 4321",
                    "hello@downtownfit.ro",
                    "xbodygym.jpg",
                    90
            ));

            roomRepository.save(new GymRoom(
                    "Mega Gym Obor",
                    "Mega Gym e locul perfect pentru antrenamente intensive și relaxare în zona de saună.",
                    "Șoseaua Colentina, nr. 2",
                    "București",
                    "Obor",
                    44.452400, 26.142700,
                    "06:00-22:00",
                    "+40 021 909 8888",
                    "support@megagym.ro",
                    "stayfit_carolina.jpg",
                    110
            ));

            roomRepository.save(new GymRoom(
                    "FitWay Drumul Taberei",
                    "Echipamente Technogym, sală de cycling și class room pentru HIIT și cardio intensiv.",
                    "Strada Valea Ialomiței, nr. 6",
                    "București",
                    "Drumul Taberei",
                    44.417300, 26.034700,
                    "06:30-23:00",
                    "+40 021 555 6666",
                    "office@fitwaydt.ro",
                    "urbanfit.jpg",
                    95
            ));

            roomRepository.save(new GymRoom(
                    "Elite Gym Aviatorilor",
                    "Fitness de lux, sală de yoga suspendată și nutriționist integrat în pachetul premium.",
                    "Bulevardul Aviatorilor, nr. 18",
                    "București",
                    "Aviatorilor",
                    44.460800, 26.083200,
                    "08:00-22:00",
                    "+40 021 333 2222",
                    "elite@aviatorigym.ro",
                    "flexzone.jpg",
                    70
            ));

            roomRepository.save(new GymRoom(
                    "Energy Gym Tineretului",
                    "Sală vibrantă cu atmosferă tânără, dotări excelente și acces facil la metrou.",
                    "Strada Cuțitul de Argint, nr. 15",
                    "București",
                    "Tineretului",
                    44.411600, 26.102100,
                    "07:00-22:00",
                    "+40 021 212 1313",
                    "info@energytineretului.ro",
                    "powerhouse.jpg",
                    85
            ));

            System.out.println("GymRoomDataInitializer: 10 săli din București au fost salvate în baza de date.");
        } else {
            System.out.println("GymRoomDataInitializer: există deja săli în baza de date. Nu adaug altele.");
        }
    }
}
