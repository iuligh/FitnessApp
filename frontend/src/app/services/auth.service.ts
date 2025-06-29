import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { StorageService } from './storage.service';

const AUTH_API = 'http://localhost:8080/api/auth/';
const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private http: HttpClient, private storageService: StorageService) {}

  login(username: string, password: string): Observable<HttpResponse<any>> {
    return this.http.post<any>(
      AUTH_API + 'signin',
      { username, password },
      {
        headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
        withCredentials: true,
        observe: 'response' as const
      }
    );
  }

  // Token key for localStorage
  private readonly TOKEN_KEY = 'auth-token';

  saveAuthData(response: any): void {
    if (!response) {
      console.error('No response data provided to saveAuthData');
      return;
    }
    
    console.log('Saving auth data:', response);
    
    // Store user data in sessionStorage
    const userData = {
      id: response.id,
      username: response.username,
      email: response.email,
      roles: response.roles || []
    };
    
    this.storageService.saveUser(userData);
    
    // The token is stored in an HTTP-only cookie, so we don't need to handle it here
    // The browser will automatically include it in subsequent requests
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  register(username: string, email: string, password: string): Observable<any> {
    return this.http.post(
      AUTH_API + 'signup',
      { username, email, password },
      httpOptions
    );
  }

  logout(): Observable<any> {
    localStorage.removeItem(this.TOKEN_KEY);
    this.storageService.clean();
    return this.http.post(AUTH_API + 'signout', {}, httpOptions);
  }
}
