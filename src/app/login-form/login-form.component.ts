import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { LoginService } from '../login.service';
import { CurrentUserService } from '../current-user.service';
import { UserCredentials } from '../user-credentials';
import { ReactiveFormsModule, FormControl, FormGroup, Validators} from '@angular/forms';
import { User } from '../user';

@Component({
  selector: 'app-login-form',
  standalone: true,
  imports: [ ReactiveFormsModule ],
  templateUrl: './login-form.component.html',
  styleUrl: './login-form.component.css'
})
export class LoginFormComponent {

 loginForm = new FormGroup({
    username: new FormControl('', [Validators.required]),
    password: new FormControl('', [Validators.required]),
  });

  userCredentials: UserCredentials

  constructor(private route: ActivatedRoute, private router: Router, private loginService: LoginService, private currentUserService: CurrentUserService) {
    this.userCredentials = new UserCredentials();
  }

  onSubmit() {
    this.userCredentials.username = this.loginForm.value.username || '';
    this.userCredentials.password = this.loginForm.value.password || '';

    this.loginService.login(this.userCredentials).subscribe({
      next: () => this.goToHome(),
      error: err => console.error('Login failed', err)
    });
  }
  goToHome() {
     this.router.navigate(['/home']);
  }
}
