import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserCredentials } from '../user-credentials';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {

  registerForm = new FormGroup({
    username: new FormControl('', [
      Validators.required,
      Validators.maxLength(30)
    ]),
    password: new FormControl('', [
      Validators.required,
      Validators.minLength(8),
      Validators.maxLength(64),
      Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/)
    ]),
    verifypassword: new FormControl('', [
      Validators.required,
    ]),
  });

  userCredentials: UserCredentials;
  secondpassword: string = '';
  usernameTaken: boolean = false;
  passwordsMatch: boolean = true;

  constructor(private route: ActivatedRoute, private router: Router, private userService: UserService) {
    this.userCredentials = new UserCredentials();
  }

  onSubmit() {
    this.userCredentials.username = this.registerForm.value.username || '';
    this.userCredentials.password = this.registerForm.value.password || '';

    this.secondpassword = this.registerForm.value.verifypassword || '';

    //check passwords match
    if (this.userCredentials.password === this.secondpassword) {
      this.userService.createUser(this.userCredentials).subscribe({
        next: () => this.goToLogin(),
        error: err => this.usernameTaken = true,
      });
    } else {
      this.passwordsMatch = false;
    }
  }

  goToLogin() {
    this.router.navigate(['/login']);
  }

  get username() {
    return this.registerForm.controls['username'];
  }
  get password() {
    return this.registerForm.controls['password'];
  }
}
