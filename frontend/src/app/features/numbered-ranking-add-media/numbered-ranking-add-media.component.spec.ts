import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NumberedRankingAddMediaComponent } from './numbered-ranking-add-media.component';

describe('NumberedRankingAddMediaComponent', () => {
  let component: NumberedRankingAddMediaComponent;
  let fixture: ComponentFixture<NumberedRankingAddMediaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NumberedRankingAddMediaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NumberedRankingAddMediaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
