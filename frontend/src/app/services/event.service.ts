import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import { Observable } from 'rxjs';

import {EWeekDays, Event } from '../models/event.model';
import {EventCreateDto} from "../models/eventDto.model";

const BASE_URL = 'http://localhost:8080/api';

@Injectable({
    providedIn: 'root',
})
export class EventService {
    constructor(private http: HttpClient) {}

    /** GET all events (EventDTO[] din backend) */
    getAll(): Observable<Event[]> {
        return this.http.get<Event[]>(`${BASE_URL}/events`);
    }

    /** GET event by ID */
    getById(id: number): Observable<Event> {
        return this.http.get<Event>(`${BASE_URL}/events/${id}`);
    }

    /** POST new event (trimit EventCreateDto) */
    create(data: EventCreateDto): Observable<Event> {
        return this.http.post<Event>(`${BASE_URL}/events`, data);
    }

    /** PUT update event (trimit EventCreateDto) */
    update(id: number, data: EventCreateDto): Observable<Event> {
        return this.http.put<Event>(`${BASE_URL}/events/${id}`, data);
    }

    /** DELETE event */
    delete(id: number): Observable<any> {
        return this.http.delete(`${BASE_URL}/events/${id}`);
    }

    /** DELETE all (dacă ai endpoint-ul) */
    deleteAll(): Observable<any> {
        return this.http.delete(`${BASE_URL}/events`);
    }

    /** Caută după titlu */
    findByTitle(title: string): Observable<Event[]> {
        return this.http.get<Event[]>(`${BASE_URL}/events?title=${title}`);
    }

  /** GET evenimente asociate unei săli (roomId) */
  getEventsByRoomId(roomId: number): Observable<Event[]> {
    return this.http.get<Event[]>(`${BASE_URL}/rooms/${roomId}/events`);
  }

  subscribe(eventId: number, userId: number): Observable<any> {
    return this.http.post(`${BASE_URL}/events/${eventId}/subscribe`, null, {
      params: { userId: userId.toString() }
    });
  }

  getProgramForUser(userId: number, day: EWeekDays): Observable<Event[]> {
    const params = new HttpParams()
      .set('userId', userId.toString())
      .set('day', day);
    return this.http.get<Event[]>(`${BASE_URL}/program`, { params });
  }

}
