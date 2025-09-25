import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { LoginService } from '../../services/login.service';
import { UserCredentialsDTO } from '../../core/dtos/user-credentials-dto';
import { ReactiveFormsModule, FormControl, FormGroup, Validators} from '@angular/forms';
import { User } from '../../core/models/user';

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

  userCredentials: UserCredentialsDTO = {
    username: '',
    password: ''
  }
  loginFailed: boolean = false;

  constructor(private route: ActivatedRoute, private router: Router, private loginService: LoginService) {

  }

  onSubmit() {

    this.userCredentials.username = this.loginForm.value.username || '';
    this.userCredentials.password = this.loginForm.value.password || '';

    this.loginService.login(this.userCredentials).subscribe({
      next: () => this.goToLanding(),
      error: err => this.loginFailed = true,
    });
  }
  goToLanding() {
    this.router.navigate(['/']);
  }

  get username() {
    return this.loginForm.controls['username'];
  }
  get password() {
    return this.loginForm.controls['password'];
  }
}
