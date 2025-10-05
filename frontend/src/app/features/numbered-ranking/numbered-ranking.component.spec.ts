import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NumberedRankingComponent } from './numbered-ranking.component';

describe('NumberedRankingComponent', () => {
  let component: NumberedRankingComponent;
  let fixture: ComponentFixture<NumberedRankingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NumberedRankingComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NumberedRankingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
