import {Component, HostListener} from '@angular/core';
import {StorageService} from "./services/storage.service";
import {AuthService} from "./services/auth.service";
import {Subscription} from "rxjs";
import {EventBusService} from "./components/shared/event-bus.service";
import {NotificationService} from "./services/notification.service";

interface AppNotification {
  id: number;
  message: string;
  read: boolean;
  createdAt: string;
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent {
  title = 'frontend';
  private roles: string[] = [];
  isLoggedIn = false;
  showAdminBoard = false;
  username?: string;

  eventBusSub?: Subscription;

  constructor(
    private storageService: StorageService,
    private authService: AuthService,
    private eventBusService: EventBusService,
    private notificationService: NotificationService,
  ) {}

  notifications: AppNotification[] = [];

  unreadCount: number = 0;
  dropdownVisible = false;

  toggleNotifications() {
    this.dropdownVisible = !this.dropdownVisible;
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent) {
    const target = event.target as HTMLElement;
    if (!target.closest('.notification-item')) {
      this.dropdownVisible = false;
    }
  }

  loadNotifications() {
    const user = this.storageService.getUser();
    if (!user?.id) return;

    this.notificationService.getNotificationsForUser(user.id).subscribe({
      next: data => {
        this.notifications = data;
        console.log (data)
        this.unreadCount = data.filter(n => !n.read).length;
      },
      error: err => {
        console.error('Eroare la încărcarea notificărilor', err);
      }
    });
  }

  markAsRead(id: number) {
    this.notifications = this.notifications.filter(n => n.id !== id);
    this.unreadCount = this.notifications.filter(n => !n.read).length;

    // backend call
    this.notificationService.markAsRead(id).subscribe({
      next: () => {},
      error: err => console.error("Eroare la marcare notificare ca citită", err)
    });
  }

  ngOnInit(): void {
    this.isLoggedIn = this.storageService.isLoggedIn();

    if (this.isLoggedIn) {
      const user = this.storageService.getUser();
      this.roles = user.roles;

      this.showAdminBoard = this.roles.includes('ROLE_ADMIN');
      this.loadNotifications();

      this.username = user.username;
    }

    this.eventBusSub = this.eventBusService.on('logout', () => {
      this.logout();
    });
  }

  logout(): void {
    this.authService.logout().subscribe({
      next: res => {
        console.log(res);
        this.storageService.clean();

        window.location.reload();
      },
      error: err => {
        console.log(err);
      }
    });
  }
}
