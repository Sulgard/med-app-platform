import { Routes } from '@angular/router';

export const routes: Routes = [
    {
        path: 'auth',
        children: [
            {path: 'login', loadComponent: () => import('./features/auth/pages/login/login').then(l => l.Login)},
            {path: 'register', loadComponent: () => import('./features/auth/pages/register/register').then(r => r.Register)},
        ]
    },
];
