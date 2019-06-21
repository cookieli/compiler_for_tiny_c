.file "tests/codeGenOfMe/if_04.dcf"
.text
.section .rodata
.LC0:
.string "hello world\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $16, %rsp
movb $0,-1(%rbp)
movb -1(%rbp),%al
cmpb $0,%al
jle .L1
.L2:
leave
ret
.L1:
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
jmp .L2

