.file "tests/codeGenOfMe/if_07.dcf"
.text
.section .rodata
.LC0:
.string "hello world\n"
.LC1:
.string "wrong, wrong, wrong\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $32, %rsp
movq $8,-26(%rbp)
movq $8,-18(%rbp)
movq $100,-10(%rbp)
movb $1,-2(%rbp)
movb $0,-1(%rbp)
movb -1(%rbp),%al
cmpb $0,%al
jle .L1
movb -2(%rbp),%al
cmpb $0,%al
jle .L2
.L1:
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
jmp .L3
.L2:
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
.L3:
leave
ret

