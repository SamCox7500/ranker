import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateNumberedRankingComponent } from './create-numbered-ranking.component';

describe('CreateNumberedRankingComponent', () => {
  let component: CreateNumberedRankingComponent;
  let fixture: ComponentFixture<CreateNumberedRankingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateNumberedRankingComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateNumberedRankingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
