// src/app/components/sali/detaliu-sala/detaliu-sala.component.ts

import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {RoomService} from '../../../services/room.service';
import {EventService} from '../../../services/event.service';
import {GymRoom} from '../../../models/gym-room.model';
import {Event} from '../../../models/event.model';
import * as L from 'leaflet';
import {StorageService} from "../../../services/storage.service";
import {MessageService} from "primeng/api";


@Component({
  selector: 'app-detaliu-sala',
  templateUrl: './detaliu-sala.component.html',
  styleUrls: ['./detaliu-sala.component.css']
})
export class DetaliuSalaComponent implements OnInit {
  roomId!: number;
  sala?: GymRoom;
  events: Event[] = [];
  errorMessage: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private roomService: RoomService,
    private eventService: EventService,
    private storage: StorageService,
    private messageService: MessageService
  ) { }

  ngOnInit(): void {
    this.roomId = +this.route.snapshot.params['id'];
    this.loadSala();
    this.loadEventsForSala();
  }

  loadSala(): void {
    this.roomService.getRoomById(this.roomId).subscribe({
      next: (data) => {
        this.sala = data;
        console.log(data)
        this.errorMessage = null;

        setTimeout(() => {
          if (this.sala?.latitude && this.sala?.longitude) {
            const map = L.map('map').setView([this.sala.latitude, this.sala.longitude], 15);
            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
              attribution: '&copy; OpenStreetMap contributors'
            }).addTo(map);

            L.marker([this.sala.latitude, this.sala.longitude])
              .addTo(map)
              .bindPopup(this.sala.name)
              .openPopup();
          }
        }, 200);
      },
      error: (err) => {
        console.error('Eroare la încărcarea sălii', err);
        this.errorMessage = 'Sala nu există sau s-a produs o eroare.';
      }
    });
  }

  loadEventsForSala(): void {
    this.eventService.getEventsByRoomId(this.roomId).subscribe({
      next: (evs: Event[]) => {
        this.events = evs;
        console.log(this.events );
      },
      error: (err) => {
        console.error('Eroare la încărcarea evenimentelor pentru sală', err);
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/rooms']);
  }

  formatTime(hms: string | undefined): string {
    // Presupunem că backend trimite "HH:mm:ss"
    if (!hms) return '';
    return hms.substring(0, 5); // "HH:mm"
  }

  enroll(evId: number) {
    const user = this.storage.getUser();
    if (!user) {
      this.messageService.add({
        severity: 'error',
        summary: 'Eroare',
        detail: 'Te rog loghează-te mai întâi.',
        life: 3000
      });
      return;
    }

    this.eventService.subscribe(evId, user.id).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Succes',
          detail: 'Te-ai înscris cu succes!',
          life: 3000
        });
      },
      error: e => {
        console.log(e)
        this.messageService.add({
          severity: 'error',
          summary: 'Eroare',
          detail: e.error?.error || 'A apărut o eroare.',
          life: 3000
        });
      }
    });
  }


  get currentUser() {
    return this.storage.getUser();
  }

  canEditEvent(ev: Event): boolean {
    const user = this.currentUser;
    if (!user) return false;

    const isAdmin = user.roles?.includes('ROLE_ADMIN');
    const isTrainer = user.fullName === ev.trainerName; // sau compari `id` dacă îl ai
    return isAdmin || isTrainer;
  }

}
