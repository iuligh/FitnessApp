import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
interface AppNotification {
  id: number;
  message: string;
  read: boolean;
  createdAt: string;
}

// notification.service.ts
@Injectable({ providedIn: 'root' })
export class NotificationService {
  private baseUrl = 'http://localhost:8080/api/notifications';

  constructor(private http: HttpClient) {}

  /** primește notificările unui user */
  getNotificationsForUser(userId: number): Observable<AppNotification[]> {
    // NOTĂ: URL-ul fără query params, direct în path:
    return this.http.get<AppNotification[]>(`${this.baseUrl}/user/${userId}`);
  }

  /** marchează o notificare ca citită */
  markAsRead(id: number): Observable<void> {
    return this.http.put<void>(`${this.baseUrl}/${id}/read`, {});
  }

  /** marchează toate notificările drept citite */
  markAllAsRead(userId: number): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/mark-read/${userId}`, {});
  }
}


