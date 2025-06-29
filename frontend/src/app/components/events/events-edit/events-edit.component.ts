// src/app/components/events/events-edit/events-edit.component.ts

import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Event } from '../../../models/event.model';
import { EventService } from '../../../services/event.service';

@Component({
  selector: 'app-events-edit',
  templateUrl: './events-edit.component.html',
  styleUrls: ['./events-edit.component.css']
})
export class EventsEditComponent implements OnInit {
  events: Event[] = [];
  currentIndex = -1;
  title = '';

  constructor(private eventService: EventService, private router: Router) {}

  ngOnInit(): void {
    this.retrieveEvents();
  }

  retrieveEvents(): void {
    this.eventService.getAll().subscribe({
      next: (data: Event[]) => {
        this.events = data;
      },
      error: (e) => console.error(e)
    });
  }

  setActiveEvent(event: Event, index: number): void {
    this.currentIndex = index;
    this.router.navigate(['/events', event.id]);
  }

  removeAllEvents(): void {
    this.eventService.deleteAll().subscribe({
      next: () => {
        this.retrieveEvents();
        this.currentIndex = -1;
      },
      error: (e) => console.error(e)
    });
  }

  searchTitle(): void {
    if (!this.title) {
      this.retrieveEvents();
      return;
    }
    this.eventService.findByTitle(this.title).subscribe({
      next: (data: Event[]) => {
        this.events = data;
      },
      error: (e) => console.error(e)
    });
  }
}
