// src/app/components/sali/sali.component.ts

import { Component, OnInit } from '@angular/core';
import { RoomService } from '../../services/room.service';
import { GymRoom } from '../../models/gym-room.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sali',
  templateUrl: './sali.component.html',
  styleUrls: ['./sali.component.css']
})
export class SaliComponent implements OnInit {
  rooms: GymRoom[] = [];
  errorMessage: string | null = null;

  constructor(private roomService: RoomService, private router: Router) { }

  ngOnInit(): void {
    this.loadRooms();
  }

  loadRooms(): void {
    this.roomService.getAllRooms().subscribe({
      next: (r: GymRoom[]) => {
        this.rooms = r;
        this.errorMessage = null;
      },
      error: (err: any) => {
        console.error('Eroare la încărcarea sălilor', err);
        this.errorMessage = 'Nu am putut încărca lista sălilor. Încearcă mai târziu.';
      }
    });
  }

  goToDetails(room: GymRoom): void {
    // navigăm către detaliu și afișăm evenimentele asociate
    this.router.navigate(['/rooms', room.id]);
  }
}
