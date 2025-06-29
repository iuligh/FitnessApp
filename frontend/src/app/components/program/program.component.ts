
import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {Event, EWeekDays} from '../../models/event.model';
import {EventService} from '../../services/event.service';
import {StorageService} from '../../services/storage.service';

@Component({
  selector: 'app-program',
  templateUrl: './program.component.html',
  styleUrls: ['./program.component.css']
})
export class ProgramComponent implements OnInit {
  days = Object.values(EWeekDays);
  selectedDay: EWeekDays = EWeekDays.LUNI;
  events: Event[] = [];

  constructor(
    private eventService: EventService,
    private storage: StorageService,
    private router: Router
  ) { }

  ngOnInit(): void {
    // Inițial pentru ziua selectată
    this.loadProgram();
  }

  getEventsByDay(day: EWeekDays): void {
    this.selectedDay = day; // Update the selected day
    this.loadProgram();
  }


  private loadProgram() {
    const user = this.storage.getUser();
    if (!user) {
      // redirecționează la login dacă nu e autentificat
      this.router.navigate(['/login']);
      return;
    }
    this.eventService.getProgramForUser(user.id, this.selectedDay).subscribe({
      next: data => this.events = data,
      error: err => console.error('Eroare la încărcarea programului', err)
    });
  }
}
