.file "tests/codeGenOfMe/hello.dcf"
.text
.section .rodata
.LC0:
.string "hello world"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $0, %rsp
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
leave
ret

