// src/app/services/room.service.ts

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { GymRoom } from '../models/gym-room.model';
import { Event } from '../models/event.model';

const BASE_URL = 'http://localhost:8080/api/rooms';

@Injectable({
  providedIn: 'root'
})
export class RoomService {
  constructor(private http: HttpClient) {}

  getAllRooms(): Observable<GymRoom[]> {
    return this.http.get<GymRoom[]>(BASE_URL, { withCredentials: true });
  }

  getRoomById(id: number): Observable<GymRoom> {
    return this.http.get<GymRoom>(`${BASE_URL}/${id}`, { withCredentials: true });
  }

  createRoom(room: GymRoom): Observable<GymRoom> {
    return this.http.post<GymRoom>(BASE_URL, room, { withCredentials: true });
  }

  deleteRoom(id: number): Observable<any> {
    return this.http.delete(`${BASE_URL}/${id}`, { withCredentials: true });
  }

  updateRoom(id: number, room: GymRoom): Observable<GymRoom> {
    return this.http.put<GymRoom>(`${BASE_URL}/${id}`, room, { withCredentials: true });
  }

  getEventsByRoomId(roomId: number): Observable<Event[]> {
    return this.http.get<Event[]>(`${BASE_URL}/${roomId}/events`, { withCredentials: true });
  }
}
