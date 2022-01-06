package com.xmug.traveldiary;


import com.xmug.traveldiary.data.Diary;
import com.xmug.traveldiary.data.room.DiaryDAO;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class MockTest {

    @Mock
    private DiaryDAO mockDiaryDAO;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testGetDiaryById() {
        Diary diary = new Diary();
        diary.setId(12345);
        diary.setTitle("My Diary");
        diary.setContent("aaaa bbbb cccc dddd");

        mockDiaryDAO.insertDiary(diary);

        when(mockDiaryDAO.getDiarybyId(anyInt())).thenReturn(diary);

        Diary thisDiary = mockDiaryDAO.getDiarybyId(12345);

        assertThat(thisDiary, equalTo(diary));
    }

    @Test
    public void testDeleteDiary() {

        mockDiaryDAO.removeDiarybyId(12345);

        List<Diary> diaryList = mockDiaryDAO.getAllDiaries();

        assertThat(diaryList.size(), equalTo(0));
    }
}
