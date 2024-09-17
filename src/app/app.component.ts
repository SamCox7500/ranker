import {Component} from '@angular/core';
import {Router} from '@angular/router'
import {RouterLink} from '@angular/router';
import {RouterOutlet} from '@angular/router';
import {RouterLinkActive} from '@angular/router';
import {HttpClient} from '@angular/common/http';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title: string;

  constructor() {
    this.title = 'Spring Boot - Test'
  }
}
