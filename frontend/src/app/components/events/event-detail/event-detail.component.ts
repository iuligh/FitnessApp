// src/app/components/events/event-detail/event-detail.component.ts

import { Component, Input, OnInit } from '@angular/core';
import { Event, EWeekDays } from '../../../models/event.model';
import { EventService } from '../../../services/event.service';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { EventCreateDto } from '../../../models/eventDto.model';

@Component({
  selector: 'app-event-detail',
  templateUrl: './event-detail.component.html',
  styleUrls: ['./event-detail.component.css']
})
export class EventDetailComponent implements OnInit {
  @Input() viewMode = false;
  currentEvent?: Event;
  message = '';

  eventDto: EventCreateDto = {
    title: '',
    description: '',
    imageName: '',
    trainerId: 0,
    startHour: undefined,
    endHour: undefined,
    days: []
  };

  days: string[] = Object.values(EWeekDays);
  selectedDays: string[] = [];
  selectedFile: File | null = null;

  startHourDate?: string; // "HH:mm"
  endHourDate?: string;   // "HH:mm"

  constructor(
      private eventService: EventService,
      private route: ActivatedRoute,
      private router: Router,
      private http: HttpClient
  ) {}

  ngOnInit(): void {
    const idParam = this.route.snapshot.params['id'];
    this.getEvent(+idParam);
  }

  getEvent(id: number): void {
    this.eventService.getById(id).subscribe({
      next: (data: Event) => {
        this.currentEvent = data;

        this.eventDto = {
          title: data.title,
          description: data.description || '',
          imageName: data.imageName || '',
          trainerId: data.trainerId,
          startHour: data.startHour,
          endHour: data.endHour,
          days: data.days
        };

        this.selectedDays = [...data.days];

        this.startHourDate = data.startHour ? data.startHour.substring(0, 5) : undefined;
        this.endHourDate = data.endHour ? data.endHour.substring(0, 5) : undefined;
      },
      error: e => console.error(e)
    });
  }

  toggleDay(day: string): void {
    if (this.selectedDays.includes(day)) {
      this.selectedDays = this.selectedDays.filter(d => d !== day);
    } else {
      this.selectedDays.push(day);
    }
    this.eventDto.days = [...this.selectedDays] as EWeekDays[];
  }

  onFileSelected(event: any): void {
    const file: File = event.target.files[0];
    if (file) {
      this.selectedFile = file;
      this.eventDto.imageName = file.name;
    }
  }

  uploadImage(file: File) {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<{ fileName: string }>(
        'http://localhost:8080/api/events/upload',
        formData
    );
  }

  saveUpdatedEvent(): void {
    if (!this.currentEvent) return;

    if (this.startHourDate) {
      this.eventDto.startHour = this.startHourDate + ':00';
    }
    if (this.endHourDate) {
      this.eventDto.endHour = this.endHourDate + ':00';
    }

    if (this.selectedFile) {
      this.uploadImage(this.selectedFile).subscribe({
        next: resp => {
          this.eventDto.imageName = resp.fileName;
          this.performUpdate(this.currentEvent!.id);
        },
        error: e => console.error(e)
      });
    } else {
      this.performUpdate(this.currentEvent.id);
    }
  }

  private performUpdate(eventId: number) {
    this.eventService.update(eventId, this.eventDto).subscribe({
      next: () => {
        this.message = 'Evenimentul a fost actualizat cu succes!';
      },
      error: e => console.error(e)
    });
  }

  deleteEvent(): void {
    if (!this.currentEvent) return;
    this.eventService.delete(this.currentEvent.id).subscribe({
      next: () => this.router.navigate(['/events']),
      error: e => console.error(e)
    });
  }

  getDays(): string[] {
    return this.currentEvent?.days || [];
  }
}
