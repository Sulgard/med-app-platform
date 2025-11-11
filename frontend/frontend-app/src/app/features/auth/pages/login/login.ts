import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../../../core/services/auth-service';
import { Router } from '@angular/router';
import { AuthRoutingModule } from "../../auth-routing-module";
import { catchError, finalize, of, take } from 'rxjs';

@Component({
  selector: 'app-login',
  imports: [
    ReactiveFormsModule,
    AuthRoutingModule
],
  templateUrl: './login.html',
  styleUrl: './login.scss'
})
export class Login implements OnInit{

  loginForm!: FormGroup;
  loading:boolean = false;
  serverError:string | null = null;


  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
  ) {
    this.loginForm = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]]
  });
  }


  
  ngOnInit(): void {
  }

  onSubmit(){
    if (this.loginForm.invalid) return;
    this.loading = true;
    this.serverError = null;

    this.authService.login(this.loginForm.value).pipe(
      take(1),
      finalize(() => this.loading = false),
      catchError(err => {
        this.serverError = err?.error?.message || 'Login error';
        return of(null);
      })
    ).subscribe((res: any) => {
      if (res) {
        if (res.accessToken) localStorage.setItem('accessToken', res.accessToken);
        if (res.refreshToken) localStorage.setItem('refreshToken', res.refreshToken);

        this.router.navigateByUrl('/');
      }
    });
  }

}
