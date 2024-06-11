import {Component} from '@angular/core';
import {Router} from '@angular/router'
import {RouterLink} from '@angular/router';
import {RouterOutlet} from '@angular/router';
import {RouterLinkActive} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {HomeComponent} from './home.component';
import {LoginComponent} from './login.component';
import {AppService} from './app.service';
import {finalize} from 'rxjs';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterLink, RouterOutlet, RouterLinkActive],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  constructor(private app: AppService, private http: HttpClient, private router: Router) {
    this.app.authenticate(undefined, undefined);
  }
  logout() {
    this.http.post('logout', {}).pipe(
      finalize(() => {
        this.app.authenticated = false;
        this.router.navigateByUrl('login');
    })
    ).subscribe();
  }
}
