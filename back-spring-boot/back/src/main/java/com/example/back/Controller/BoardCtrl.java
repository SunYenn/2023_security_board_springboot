package com.example.back.Controller;

import com.example.back.Repository.BoardMapper;
import com.example.back.Service.AuthUtil;
import com.example.back.VO.BoardVO;
import com.example.back.VO.CommentVO;
import com.example.back.VO.PagingVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;


@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/board")
public class BoardCtrl {

    @Autowired
    private BoardMapper mapper;

    @GetMapping("/list") // 글 목록 불러오기
    public PagingVO board_list(PagingVO vo) {

        int total_count = mapper.countBoard(vo);
        PagingVO board_list = new PagingVO(vo.getPage_size(), total_count, vo.getCurrent_page());
        board_list.setSearch_obj(vo.getSearch_obj());
        board_list.setSearch_category(vo.getSearch_category());
        board_list.setBoardList(mapper.selectBoardList(board_list));

        return board_list;
    }

    @GetMapping("/increment/{idx}") // 조회수 증가
    public void increment(@PathVariable int idx) {
        mapper.incrementViews(idx);
    }

    @GetMapping("/view/{idx}") // 글 한건 불러오기
    public ResponseEntity<?> board_view(@PathVariable int idx) {
        BoardVO board_view = mapper.selectBoardByIdx(idx);
        if (board_view != null) {
            board_view.setCommentList(mapper.selectCommentByIdx(idx));
            return ResponseEntity.ok(board_view);
        }
        else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("잘못된 접근입니다.");
    }

    @GetMapping("/delete/{idx}") // 글 삭제
    public void delete(@PathVariable int idx) {
        mapper.deleteCommentByIdx(idx);
        mapper.deleteBoardByIdx(idx);
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data") // 파일 업로드
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {

        int fileCount = mapper.countBoardByOrifilename(file.getOriginalFilename());

        String location = "D:/upload/board/";
        String filename = (fileCount > 0) ? "(" + fileCount + ")" + file.getOriginalFilename() : file.getOriginalFilename();
        try {
            File savedFile = new File(location + filename);
            file.transferTo(savedFile);
            return ResponseEntity.ok(filename);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file.");
        }

    }

    @PostMapping("/insert") // 글 등록
    public void insert(@RequestBody BoardVO vo) {
        vo.setNickname(AuthUtil.getNickname()); // 토큰의 이름 값 지정
        mapper.insertBoard(vo);
    }

    @PostMapping("/download") // 파일 다운로드
    public void downloadFile(
            @RequestBody Map<String, String> requestData,
            HttpServletResponse response) throws IOException
    {
        String fileName = requestData.get("fileName");
        String filePath = "D:/upload/board/" + fileName; // 파일 경로 생성

        File file = new File(filePath);
        InputStream inputStream = new FileInputStream(file); // 파일을 읽기 위한 개게 생성

        response.setContentType("application/octet-stream"); // 다운로드할 파일의 바이너리 데이터임을 설정
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        // 파일의 데이터를 읽어 응답의 출력 스트림으로 복사
        IOUtils.copy(inputStream, response.getOutputStream());

        //응답 버퍼를 비워서 데이터를 클라이언트로 전송
        response.flushBuffer();
    }

    @PostMapping("/update") // 글 수정
    public void update(@RequestBody BoardVO vo) {
        mapper.updateBoard(vo);
    }

    @PostMapping("/comment/insert") // 댓글 등록
    public void commentInsert(@RequestBody CommentVO vo) {
        vo.setNickname(AuthUtil.getNickname()); // 로그인 계정 정보 값 얻어와서 이름 지정
        if (vo.getStep() == 1) {
            vo.setGup(mapper.getGup(vo.getBoard_idx()) + 1);
        } else {
            mapper.setSeqs(vo);
        }
        vo.setContents(vo.getContents()
                .replaceAll("%20", " ")
                .replaceAll("%3C", "<")
                .replaceAll("%3E", ">")
                .replaceAll("%0a", "\n"));
        mapper.insertComment(vo);
    }

    @PostMapping("/comment/delete/{idx}") // 댓글 삭제
    public void commentDelete(@PathVariable int idx) {
        mapper.deleteComment(idx);
    }

    @PostMapping("/comment/restore/{idx}") // 댓글 복구
    public void commentRestore(@PathVariable int idx) {
        mapper.restoreComment(idx);
    }

    @PostMapping("/comment/update") // 댓글 수정
    public void commentUpdate(@RequestBody CommentVO vo) {
        mapper.updateComment(vo);
    }

}
