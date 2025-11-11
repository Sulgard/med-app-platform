import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

const API_BASE_URL = 'http://localhost:8080/';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  constructor(private http: HttpClient){}

  login(credentials : {email: string, password: string}): Observable<any> {
    return this.http.post(`${API_BASE_URL}/auth/login`, credentials)
  }

  logout() {
    const refreshToken = localStorage.getItem('refreshToken');
    if (refreshToken) {
      this.http.post(`${API_BASE_URL}/auth/logout`, {refreshToken}).subscribe();
    }
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
  }

  refreshAccessToken(refreshToken: string) {
    return this.http.post(`${API_BASE_URL}/auth/refresh`, { refreshToken });
  }

  async getProtectedData() {
    return this.http.get(`${API_BASE_URL}/protected/data`);  }
  
}
