// src/app/components/events/event-add/event-add.component.ts
import { Component } from '@angular/core';
import { EWeekDays } from '../../../models/event.model';
import { EventService } from '../../../services/event.service';
import { HttpClient } from '@angular/common/http';
import { EventCreateDto } from '../../../models/eventDto.model';

@Component({
  selector: 'app-event-add',
  templateUrl: './event-add.component.html',
  styleUrls: ['./event-add.component.css']
})
export class EventAddComponent {
  eventDto: EventCreateDto = {
    title: '',
    description: '',
    imageName: '',
    trainerId: 0,
    startHour: undefined,
    endHour: undefined,
    days: []
  };
  submitted = false;
  selectedFile: File | null = null;
  tempStart: Date | null = null;
  tempEnd: Date | null = null;
  days: string[] = Object.values(EWeekDays);
  selectedDays: string[] = [];
  defaultStartHour: Date = new Date();

  constructor(private eventService: EventService, private http: HttpClient) {
    this.defaultStartHour.setMinutes(30);
  }

  saveEvent(): void {
    this.eventDto.days = [...this.selectedDays] as EWeekDays[];
    if (this.tempStart) {
      const h = this.tempStart.getHours().toString().padStart(2, '0');
      const m = this.tempStart.getMinutes().toString().padStart(2, '0');
      this.eventDto.startHour = `${h}:${m}:00`;
    }
    if (this.tempEnd) {
      const h = this.tempEnd.getHours().toString().padStart(2, '0');
      const m = this.tempEnd.getMinutes().toString().padStart(2, '0');
      this.eventDto.endHour = `${h}:${m}:00`;
    }
    this.eventService.create(this.eventDto).subscribe({
      next: () => this.submitted = true,
      error: e => console.error(e)
    });
  }

  newEvent(): void {
    this.submitted = false;
    this.eventDto = {
      title: '',
      description: '',
      imageName: '',
      trainerId: 0,
      startHour: undefined,
      endHour: undefined,
      days: []
    };
    this.selectedFile = null;
    this.tempStart = null;
    this.tempEnd = null;
    this.selectedDays = [];
  }

  onFileSelected(event: any): void {
    const file: File = event.target.files[0];
    if (file) {
      this.selectedFile = file;
      this.eventDto.imageName = file.name;
      const formData = new FormData();
      formData.append('file', file);
      this.http.post('http://localhost:8080/api/events/upload', formData).subscribe({
        next: (response: any) => this.eventDto.imageName = response.fileName,
        error: e => console.error(e)
      });
    }
  }

  toggleDay(day: string): void {
    if (this.selectedDays.includes(day)) {
      this.selectedDays = this.selectedDays.filter(d => d !== day);
    } else {
      this.selectedDays.push(day);
    }
  }
}
