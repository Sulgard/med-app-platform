import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [],
  templateUrl: './header.html',
  styleUrl: './header.scss'
})
export class HeaderComponent {
  @Input() pageTitle!: string;
  @Input() logoSrc!: string;
}
