import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth-service';
import { catchError, switchMap, throwError } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('accessToken');
  const authService = inject(AuthService);

  if (!authService) {
    throw new Error ('AuthService is not provided!');
  }

  const newRequest = token ? req.clone({
    setHeaders: {
      Authorization: `Bearer  ${token}`
    }
  })
  : req;

  return next(newRequest).pipe(
    catchError((error) => {
      if (error.status === 401) {
        const refreshToken = localStorage.getItem('refreshToken');
        if (refreshToken) {
          return authService.refreshAccessToken(refreshToken).pipe(
            switchMap((result: any) => {
              localStorage.setItem('accessToken', result.accessToken);
              const retryRequest = req.clone({setHeaders: {Authorization: `Bearer ${result.accessToken}`}});
              return next(retryRequest);
            }),
            catchError(() => {
              authService.logout();
              window.location.href = '/login';
              return throwError(() => error);
            })
          );
        } else {
          authService.logout();
          window.location.href = '/login';
        }
      }
      return throwError(() => error);
    })
  );
};
