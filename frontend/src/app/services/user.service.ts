import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/user.model';

const API_USER = 'http://localhost:8080/api/user/';
const API_URL = 'http://localhost:8080/api/test/';
@Injectable({ providedIn: 'root' })
export class UserService {
  constructor(private http: HttpClient) { }

  getPublicContent(): Observable<any> {
    return this.http.get(API_URL + 'all', { responseType: 'text' });
  }

  getUserBoard(): Observable<any> {
    return this.http.get(API_URL + 'user', { responseType: 'text' });
  }

  getModeratorBoard(): Observable<any> {
    return this.http.get(API_URL + 'mod', { responseType: 'text' });
  }

  getAdminBoard(): Observable<any> {
    return this.http.get(API_URL + 'admin', { responseType: 'text' });
  }

  getUser(id: number): Observable<User> {
    return this.http.get<User>(`${API_USER}${id}`);
  }

  updateProfile(id: number, data: Partial<User>): Observable<User> {
    return this.http.put<User>(`${API_USER}${id}`, data);
  }

  uploadProfileImage(id: number, file: File): Observable<{ profileImageUrl: string }> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<{ profileImageUrl: string }>(`${API_USER}${id}/upload-image`, formData);
  }
}
