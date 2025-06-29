// src/app/models/event-create-dto.model.ts

import { EWeekDays } from './event.model';

/**
 * DTO folosit la create/update:
 *   - nu are `id`, nu are `trainerName`
 *   - are doar `trainerId` (Long în backend)
 *   - are `startHour`/`endHour` ca string "HH:mm:ss"
 *   - are `days` ca array de EWeekDays
 */
export interface EventCreateDto {
    title: string;
    description?: string;
    imageName?: string;
    trainerId: number;
    startHour?: string;   // e.g. "10:00:00"
    endHour?: string;     // e.g. "12:00:00"
    days: EWeekDays[];
}
