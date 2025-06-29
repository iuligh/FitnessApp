import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { AuthService } from './auth.service';

export interface SubscriptionPlan {
  id: number;
  name: string;
  displayName: string;
  description: string;
  price: number;
  currency: string;
  billingCycleMonths: number;
  active: boolean;
  maxGymAccessPerWeek: number;
  personalTrainerIncluded: boolean;
  allGymsAccess: boolean;
  groupClassesIncluded: boolean;
  personalTrainingSessions: number;
}

export interface CheckoutSession {
  sessionId: string;
  sessionUrl: string;
  publicKey: string;
}

@Injectable({
  providedIn: 'root'
})
export class SubscriptionService {
  private apiUrl = `${environment.apiUrl}/subscriptions`;

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) { }

  getActivePlans(): Observable<SubscriptionPlan[]> {
    return this.http.get<any>(`${this.apiUrl}/plans`, { withCredentials: true, observe: 'response' }).pipe(
      map(response => {
        if (response.status === 200 && response.body) {
          return response.body as SubscriptionPlan[];
        }
        throw new Error('Răspuns neașteptat de la server');
      }),
      catchError((error: any) => {
        console.error('Error fetching subscription plans:', error);
        let errorMessage = 'Eroare la încărcarea abonamentelor';
        
        if (error.status === 0) {
          errorMessage = 'Nu s-a putut conecta la server. Verificați conexiunea la internet.';
        } else if (error.status === 500) {
          // Try to extract the detailed error message from the response
          const serverError = error.error;
          if (typeof serverError === 'string') {
            errorMessage = serverError;
          } else if (serverError && serverError.message) {
            errorMessage = serverError.message;
          } else {
            errorMessage = 'Eroare internă a serverului. Vă rugăm contactați asistența.';
          }
        }
        
        return throwError(() => new Error(errorMessage));
      })
    );
  }

  getPlanById(planId: number): Observable<SubscriptionPlan> {
    return this.http.get<SubscriptionPlan>(`${this.apiUrl}/plans/${planId}`);
  }

  createCheckoutSession(planId: number, successUrl: string, cancelUrl: string): Observable<CheckoutSession> {
    console.log('Creating checkout session for plan:', planId);
    
    // The JWT token will be automatically included in the cookie
    return this.http.post<CheckoutSession | { error: string }>(
      `${this.apiUrl}/checkout/${planId}`,
      null,
      { 
        params: { 
          successUrl: successUrl,
          cancelUrl: cancelUrl
        },
        withCredentials: true, // Important for sending cookies with the request
        observe: 'response' as const
      }
    ).pipe(
      map((response) => {
        if (!response.body) {
          throw new Error('Empty response from server');
        }
        
        const body = response.body;
        if ('error' in body) {
          throw new Error(body.error);
        }
        
        return body as CheckoutSession;
      }),
      catchError((error: HttpErrorResponse) => {
        console.error('Error in createCheckoutSession:', error);
        
        let errorMessage = 'Failed to create checkout session';
        
        if (error.status === 401) {
          errorMessage = 'Nu sunteți autentificat. Vă rugăm să vă autentificați.';
          // Redirect to login with return URL
          const returnUrl = encodeURIComponent(window.location.pathname);
          window.location.href = `/login?returnUrl=${returnUrl}`;
        } else if (error.error?.message) {
          errorMessage = error.error.message;
        } else if (error.status === 0) {
          errorMessage = 'Nu s-a putut conecta la server. Verificați conexiunea la internet.';
        }
        
        return throwError(() => new Error(errorMessage));
      })
    );
  }
}
