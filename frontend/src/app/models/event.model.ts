// src/app/models/event.model.ts

export enum EWeekDays {
    LUNI = "Luni",
    MARTI = "Marti",
    MIERCURI = "Miercuri",
    JOI = "Joi",
    VINERI = "Vineri",
    SAMBATA = "Sambata",
    DUMINICA = "Duminica"
}

/**
 * Interfață care oglindește EXACT structura EventDTO din backend,
 * așa cum îl trimite servlet-ul Spring:
 *   - trainerId și trainerName (nu un obiect nested trainer)
 *   - nu există room
 *   - startHour/endHour vin ca string (JSON serializat din LocalTime)
 */
export interface Event {
    id: number;
    title: string;
    description?: string;
    imageName?: string;
    trainerId: number;      // trimis în request, primit tot în DTO
    trainerName: string;    // venit în response
    trainerImage: string;    // venit în response
    startHour: string;      // "HH:mm:ss"
    endHour: string;        // "HH:mm:ss"
    days: EWeekDays[];      // ex. ["Luni","Miercuri"]
}
