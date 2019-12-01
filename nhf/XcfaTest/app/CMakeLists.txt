cmake_minimum_required(VERSION 3.4.1)

file(GLOB vixl_src
        "src/main/cpp/lib/vixl-3.0.0/src/*.cc"
        "src/main/cpp/lib/vixl-3.0.0/src/*/*.cc"
        )



include_directories(src/main/cpp/lib/vixl-3.0.0/src)
include_directories(src/main/cpp/lib/vixl-3.0.0/src/aarch64)
link_libraries(log)
add_library( xcfa
        SHARED
        src/main/cpp/launch_xcfa.cpp
        src/main/cpp/process.cpp
        src/main/cpp/assembler.cpp
        ${vixl_src})
