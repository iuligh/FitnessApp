import { Injectable } from '@angular/core';
import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpErrorResponse, HTTP_INTERCEPTORS } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';
import { StorageService } from '../services/storage.service';


@Injectable()
export class HttpRequestInterceptor implements HttpInterceptor {
  constructor(private router: Router, private storageService: StorageService) {}

  private readonly TOKEN_KEY = 'auth-token';

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Clone the request to add headers if needed
    let authReq = req;
    
    // Only add Content-Type for JSON requests if not already set and not a FormData request
    if (!req.headers.has('Content-Type') && !(req.body instanceof FormData)) {
      authReq = req.clone({
        withCredentials: true,  // Important for sending cookies
        headers: req.headers.set('Content-Type', 'application/json')
      });
    } else {
      // For FormData requests, ensure withCredentials is set
      authReq = req.clone({
        withCredentials: true  // Important for sending cookies
      });
    }
    
    return next.handle(authReq).pipe(
      catchError((error: HttpErrorResponse) => {
        console.error('HTTP Error:', error);
        
        if (error.status === 401) {
          console.log('Authentication error, cleaning storage and redirecting to login');
          this.storageService.clean();
          localStorage.removeItem(this.TOKEN_KEY);
          
          // Don't redirect if already on login page to avoid infinite loop
          if (!this.router.url.startsWith('/login') && !this.router.url.startsWith('/auth')) {
            const returnUrl = this.router.url;
            this.router.navigate(['/login'], { 
              queryParams: { 
                returnUrl: returnUrl,
                sessionExpired: true
              } 
            });
          }
        }
        
        // Re-throw the error to be handled by the component
        return throwError(() => error);
      })
    );
  }
}

export const httpInterceptorProviders = [
  { provide: HTTP_INTERCEPTORS, useClass: HttpRequestInterceptor, multi: true },
];
