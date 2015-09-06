/**
 * Marabou - Audio Tagger
 * <p>
 * Copyright (C) 2012 - 2015 Jan-Hendrik Peters
 * <p>
 * https://github.com/hennr/marabou
 * <p>
 * Marabou is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */
package com.github.marabou.audio.store;

import com.github.marabou.audio.AudioFile;
import com.github.marabou.audio.AudioFileFactory;
import com.github.marabou.ui.events.FilesSelectedEvent;
import com.github.marabou.ui.events.SaveSelectedFilesEvent;
import com.google.common.eventbus.EventBus;
import com.mpatric.mp3agic.Mp3File;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AudioFileStoreTest {

    private String storedAudioFileFilePath = "/path";

    @Test
    public void getsTheSameAudioFileByFilePathAfterStoring() throws Exception {
        // given
        AudioFileFactory fileFactoryMock = mock(AudioFileFactory.class);
        AudioFileStore audioFileStore = new AudioFileStore(new EventBus(), fileFactoryMock);

        File dummyFile = mock(File.class);
        AudioFile audioFile = new AudioFile(storedAudioFileFilePath);
        when(fileFactoryMock.createAudioFile(any(File.class))).thenReturn(audioFile);

        // when
        audioFileStore.addFile(dummyFile);
        AudioFile result = audioFileStore.getAudioFileByFilePath(storedAudioFileFilePath);

        // then
        assertEquals(audioFile, result);
    }

    @Test
    public void getStoredFilesReturnsCorrectResults() {
        // given
        AudioFileFactory fileFactoryMock = mock(AudioFileFactory.class);
        AudioFileStore audioFileStore = new AudioFileStore(new EventBus(), fileFactoryMock);

        File dummyFile = mock(File.class);
        AudioFile audioFile = new AudioFile(storedAudioFileFilePath);
        when(fileFactoryMock.createAudioFile(any(File.class))).thenReturn(audioFile);

        // when
        audioFileStore.addFile(dummyFile);

        // then
        assertEquals(1, audioFileStore.getStoredAudioFiles().size());
        assertEquals(storedAudioFileFilePath, audioFileStore.getStoredAudioFiles().get(0).getFilePath());
    }

    @Test
    public void canRemoveAudioFile() throws Exception {
        // given
        AudioFileFactory fileFactoryMock = mock(AudioFileFactory.class);
        AudioFileStore audioFileStore = new AudioFileStore(new EventBus(), fileFactoryMock);

        File dummyFile = mock(File.class);
        AudioFile audioFile = new AudioFile(storedAudioFileFilePath);
        when(fileFactoryMock.createAudioFile(any(File.class))).thenReturn(audioFile);

        // when
        audioFileStore.addFile(dummyFile);
        audioFileStore.removeAudioFile(storedAudioFileFilePath);

        // then
        assertNull(audioFileStore.getAudioFileByFilePath(storedAudioFileFilePath));
        assertEquals(0, audioFileStore.audioFiles.size());
    }

    @Test
    public void doesNotStoreTheSameFileMoreThanOnce() {
        // given
        AudioFileFactory fileFactoryMock = mock(AudioFileFactory.class);
        AudioFileStore audioFileStore = new AudioFileStore(new EventBus(), fileFactoryMock);

        File dummyFile = mock(File.class);
        AudioFile audioFile = new AudioFile("/path");
        when(fileFactoryMock.createAudioFile(any(File.class))).thenReturn(audioFile);

        // when
        audioFileStore.addFile(dummyFile);
        audioFileStore.addFile(dummyFile);

        // then
        assertEquals(1, audioFileStore.audioFiles.size());
    }

    @Test
    public void remembersSelectedFilesOnFilesSelectedEvent() {
        // given
        AudioFileFactory fileFactoryMock = mock(AudioFileFactory.class);
        EventBus bus = new EventBus();
        AudioFileStore audioFileStore = new AudioFileStore(bus, fileFactoryMock);

        AudioFile audioFile = new AudioFile("/path");
        when(fileFactoryMock.createAudioFile(any(File.class))).thenReturn(audioFile);

        Set<AudioFile> selectedFiles = new HashSet<>(1);
        selectedFiles.add(audioFile);

        // when
        bus.post(new FilesSelectedEvent(selectedFiles));

        // then
        Set<AudioFile> result = audioFileStore.getSelectedAudioFiles();
        assertEquals(1, result.size());
        assertTrue(result.contains(audioFile));
    }

    @Test
    public void forgetsAboutPreviouslySelectedFilesOnNewFilesSelectedEvent() {
        // given
        AudioFileFactory fileFactoryMock = mock(AudioFileFactory.class);
        EventBus bus = new EventBus();
        AudioFileStore audioFileStore = new AudioFileStore(bus, fileFactoryMock);

        AudioFile audioFileOne = new AudioFile("/one");
        when(fileFactoryMock.createAudioFile(any(File.class))).thenReturn(audioFileOne);

        Set<AudioFile> selectedFiles = new HashSet<>(1);
        selectedFiles.add(audioFileOne);

        // when
        bus.post(new FilesSelectedEvent(selectedFiles));

        // next event
        AudioFile audioFileTwo = new AudioFile("/two");
        selectedFiles.clear();
        selectedFiles.add(audioFileTwo);
        bus.post(new FilesSelectedEvent(selectedFiles));

        // then
        Set<AudioFile> result = audioFileStore.getSelectedAudioFiles();
        assertTrue(result.contains(audioFileTwo));
        assertFalse(result.contains(audioFileOne));
    }

    @Test
    public void aSaveSelectedFilesEventProvokesNewAudioFileSavedEvent() throws Exception {
        // given
        AudioFile audioFileMock = mock(AudioFile.class);
        Mp3File mp3FileMock = mock(Mp3File.class);

        AudioFileFactory audioFileFactoryMock = mock(AudioFileFactory.class);
        when(audioFileFactoryMock.createAudioFile(any(Mp3File.class))).thenReturn(audioFileMock);
        when(audioFileFactoryMock.createMp3File(any())).thenReturn(mp3FileMock);

        EventBus eventBusMock = mock(EventBus.class);

        AudioFileStore audioFileStore = new AudioFileStore(eventBusMock, audioFileFactoryMock) {
            @Override
            public String saveMp3File(Mp3File mp3File) {
                return "/newPath";
            }
        };

        AudioFile audioFile = new AudioFile("/path").withAlbum("album");
        Set<AudioFile> fileSet = new HashSet<>(1);
        fileSet.add(audioFile);
        audioFileStore.currentlySelectedFiles = fileSet;

        // when
        audioFileStore.onSaveSelectedFiles(new SaveSelectedFilesEvent());

        // then
        ArgumentCaptor<AudioFileSavedEvent> argument = ArgumentCaptor.forClass(AudioFileSavedEvent.class);
        verify(eventBusMock).post(argument.capture());
        assertEquals("/path", argument.getValue().oldFilePath);
        assertEquals("/newPath", argument.getValue().newFilePath);
    }
}
