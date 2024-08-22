package com.company.map.vo;
import org.springframework.web.multipart.MultipartFile;
import lombok.Setter;
import lombok.Getter;

@Setter
@Getter
public class CollectorVo {
	MultipartFile opendrive;
	String coordinate;
	String situation;
}